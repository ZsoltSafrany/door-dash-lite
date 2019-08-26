package safi.doordashlite.util.extension

import android.view.View

var View.visible: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }
    set(visible) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }
