package safi.doordashlite.util.extension

import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

fun Disposable.bind(disposables: DisposableContainer) {
    disposables.add(this)
}
