package mhashim6.android.putback.ui.notionsFragment

import android.content.res.Resources
import androidx.annotation.DrawableRes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.realm.OrderedCollectionChangeSet
import mhashim6.android.putback.data.Notion
import mhashim6.android.putback.data.NotionsRealm
import mhashim6.android.putback.ui.colorSelector
import mhashim6.android.putback.ui.intervalString
import mhashim6.android.putback.ui.statusIconSelector
import mhashim6.android.putback.ui.visibility

/**
 * Created by mhashim6 on 01/09/2018.
 */

class ViewModel(
        val notionsChanges: Observable<Pair<List<NotionCompactViewModel>, OrderedCollectionChangeSet>>,
        val emptyNotionsVisibility: Observable<Int>,
        val archives: Disposable)

class NotionCompactViewModel(
        resources: Resources,
        model: Notion,
        val notionId: String = model.id,
        val content: String = model.content,
        val interval: String = intervalString(model.interval, model.timeUnit, resources),
        @DrawableRes val statusIcon: Int = statusIconSelector(model),
        val color: Int = colorSelector(model, resources)
)

fun present(
        idleStates: PublishSubject<Pair<NotionCompactViewModel, Boolean>>,
        resources: Resources,
        isIdle: Boolean): ViewModel {

    val fillerViewVisibility = PublishSubject.create<Int>()

    val notionsChanges = NotionsRealm.notionsChanges(isIdle)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { fillerViewVisibility.onNext(it.first.isEmpty().visibility) }
            .map { it.first.map { notion -> NotionCompactViewModel(resources, notion) } to it.second }

//		successful archives
    val archives = idleStates
            .subscribe {
                val (notion, idleState) = it
                NotionsRealm.changeIdleState(notion.notionId, idleState)
            }

    return ViewModel(notionsChanges,
            fillerViewVisibility.observeOn(AndroidSchedulers.mainThread()),
            archives)
}
