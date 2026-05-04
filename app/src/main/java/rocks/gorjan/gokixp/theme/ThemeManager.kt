package rocks.gorjan.gokixp.theme

import android.content.Context
import androidx.core.content.edit
import rocks.gorjan.gokixp.MainActivity
import rocks.gorjan.gokixp.R

/**
 * Sealed class representing available themes.
 * toString() returns the exact string stored in SharedPreferences for backward compatibility.
 */
sealed class AppTheme {
    object WindowsXP : AppTheme() {
        override fun toString() = "Windows XP"
    }

    object WindowsClassic : AppTheme() {
        override fun toString() = "Windows Classic"
    }

    object WindowsVista : AppTheme() {
        override fun toString() = "Windows Vista"
    }

    object Windows11 : AppTheme() {
        override fun toString() = "Windows 11"
    }

    companion object {
        /**
         * Converts string from SharedPreferences to AppTheme.
         * Always returns Windows11 now — legacy themes removed.
         */
        fun fromString(value: String?): AppTheme = Windows11

        /**
         * Returns all available themes.
         */
        fun all(): List<AppTheme> = listOf(Windows11)
    }
}

/**
 * Centralized theme management class.
 * Now hardcoded to Windows 11 dark theme with user-customizable accent color.
 */
class ThemeManager(private val context: Context) {
    private val prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Always returns Windows 11 theme.
     */
    fun getSelectedTheme(): AppTheme = AppTheme.Windows11

    /**
     * Sets the selected theme (no-op now — always Win11).
     */
    fun setSelectedTheme(theme: AppTheme) {
        prefs.edit {
            putString(KEY_SELECTED_THEME, AppTheme.Windows11.toString())
        }
    }

    fun isClassicTheme(): Boolean = false
    fun isXPTheme(): Boolean = false
    fun isVistaTheme(): Boolean = false
    fun isWin11Theme(): Boolean = true

    // ========== Accent Color System ==========

    /**
     * Gets the user's accent color as an ARGB int.
     * Default: red (#FFFF4444)
     */
    fun getAccentColor(): Int {
        return prefs.getInt(KEY_ACCENT_COLOR, 0xFFFF4444.toInt())
    }

    /**
     * Sets the user's accent color.
     */
    fun setAccentColor(color: Int) {
        prefs.edit { putInt(KEY_ACCENT_COLOR, color) }
    }

    // ========== Resource Mapping Methods ==========

    fun getThemeStyleRes(theme: AppTheme): Int = R.style.Theme_GokiXP_Win11

    fun getTaskbarLayoutRes(theme: AppTheme): Int = R.layout.taskbar_11

    fun getStartMenuLayoutRes(theme: AppTheme): Int = R.layout.start_menu_11

    fun getDialogLayoutRes(theme: AppTheme): Int = R.layout.windows_dialog_content_11

    fun getSpinnerItemLayoutRes(theme: AppTheme): Int = R.layout.spinner_item_11

    fun getSpinnerDropdownLayoutRes(theme: AppTheme): Int = R.layout.spinner_dropdown_item_11

    fun getIELayout(): Int = R.layout.program_internet_explorer_7

    fun getIEIcon(): Int = R.drawable.ie7

    fun getWindowsIcon(): Int = R.drawable.logo_vista

    fun getRegeditIcon(): Int = R.drawable.regedit_icon_vista

    fun getSolitareIcon(): Int = R.drawable.solitare_icon_vista

    fun getWinampIcon(): Int = R.drawable.winamp_icon_xp

    fun getWmpIcon(): Int = R.drawable.wmp_vista_icon

    fun getPhotosIcon(): Int = R.drawable.photos_vista_icon

    fun getMinesweeperIcon(): Int = R.drawable.minesweeper_icon_vista

    fun getNotepadIcon(): Int = R.drawable.notepad_icon_vista

    fun getClockIcon(): Int = R.drawable.icon_clock_vista

    fun getMsnIcon(): Int = R.drawable.msn_icon_vista

    fun getMyComputerIcon(): Int = R.drawable.my_computer_vista_icon

    fun getFileGenericIcon(): Int = R.drawable.file_generic_vista

    fun getFileImageIcon(): Int = R.drawable.file_image_vista

    fun getPDFImageIcon(): Int = R.drawable.file_pdf_vista

    fun getFileAudioIcon(): Int = R.drawable.file_audio_vista

    fun getFileVideoIcon(): Int = R.drawable.file_video_vista

    fun getDriveFloppyIcon(): Int = R.drawable.drive_floppy_vista

    fun getDriveLocalIcon(): Int = R.drawable.drive_local_vista

    fun getDriveOpticalIcon(): Int = R.drawable.drive_optical_vista

    fun getWmpLayout(): Int = R.layout.program_wmp_vista

    fun getMaximizeIcon(): Int = R.drawable.window_maximize

    fun getRestoreIcon(): Int = R.drawable.window_maximize

    fun getTaskbarButtonLayoutRes(theme: AppTheme): Int = R.layout.taskbar_button_11

    fun getWindowsExplorerLayoutRes(theme: AppTheme): Int = R.layout.windows_explorer_11

    // ========== Icon Resource Mappings ==========

    fun getFolderIconRes(theme: AppTheme): Int = R.drawable.folder_vista

    fun getRecycleBinIconRes(theme: AppTheme, isEmpty: Boolean): Int = R.drawable.recycle_vista

    fun getStartButtonRes(theme: AppTheme): Int = R.drawable.logo_vista

    // ========== Font Resource Mappings ==========

    fun getPrimaryFontRes(theme: AppTheme): Int = R.font.segoeui_font_family

    fun getBoldFontRes(theme: AppTheme): Int = R.font.segoeui_bold

    // ========== Scrollbar Styling ==========

    fun applyThemedScrollbars(view: android.view.View, theme: AppTheme = getSelectedTheme()) {
        val trackRes = R.drawable.win11_scrollbar_track
        val thumbRes = R.drawable.win11_scrollbar_thumb

        val trackDrawable = androidx.core.content.ContextCompat.getDrawable(context, trackRes) ?: return
        val thumbDrawable = androidx.core.content.ContextCompat.getDrawable(context, thumbRes) ?: return

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val scrollBarSize = (6 * context.resources.displayMetrics.density).toInt()
            view.isVerticalScrollBarEnabled = true
            view.scrollBarStyle = android.view.View.SCROLLBARS_OUTSIDE_OVERLAY
            view.isScrollbarFadingEnabled = true

            when (view) {
                is android.widget.TextView -> view.scrollBarSize = scrollBarSize
                is androidx.recyclerview.widget.RecyclerView -> view.scrollBarSize = scrollBarSize
            }

            view.setVerticalScrollbarThumbDrawable(thumbDrawable)
            view.setVerticalScrollbarTrackDrawable(trackDrawable)
        }
    }

    companion object {
        private const val KEY_SELECTED_THEME = "selected_theme"
        private const val KEY_ACCENT_COLOR = "accent_color"
    }
}
