package eu.kanade.tachiyomi.ui.migration.manga.design

import android.os.Bundle
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.source.SourceManager
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import uy.kohesive.injekt.injectLazy

class MigrationSourceAdapter(
    var items: List<MigrationSourceItem>,
    controllerPre: PreMigrationController,
) : FlexibleAdapter<MigrationSourceItem>(
    items,
    controllerPre,
    true,
) {

    val isMultiLanguage =
        Injekt.get<PreferencesHelper>().enabledLanguages().get().filterNot { it == "all" }.size > 1

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(
            SELECTED_SOURCES_KEY,
            ArrayList(
                currentItems.map {
                    it.asParcelable()
                },
            ),
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val sourceManager: SourceManager by injectLazy()
        savedInstanceState.getParcelableArrayList<MigrationSourceItem.ParcelableSI>(
            SELECTED_SOURCES_KEY,
        )?.let {
            updateDataSet(it.map { MigrationSourceItem.fromParcelable(sourceManager, it) })
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    fun updateItems() {
        items = currentItems
    }

    companion object {
        private const val SELECTED_SOURCES_KEY = "selected_sources"
    }
}
