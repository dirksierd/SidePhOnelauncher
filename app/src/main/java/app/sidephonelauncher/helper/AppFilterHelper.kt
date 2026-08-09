package app.sidephonelauncher.helper

import app.sidephonelauncher.data.AppModel

interface AppFilterHelper {
    fun onAppFiltered(items:List<AppModel>)
}