# Windows 11 Theme Overhaul - Task Plan

## GOAL
Replace XP/Classic/Vista themes with a single Windows 11 dark theme w/ user-customizable accent color (red default). Make everything smoother.

## REFERENCE IMAGES
- Dark taskbar with centered icons, search bar, rounded corners
- Win11 dark File Explorer with sidebar (Quick Access, Recent files)
- Notification/Quick Settings panel (Wi-Fi, BT, brightness, volume)
- Win11 window title bars (rounded, minimize/maximize/close)
- Modern Recycle Bin icon

## ARCHITECTURE APPROACH
Since the theming system uses `ThemeManager` + layout variants + `AppTheme` sealed class, the cleanest approach is:

1. **Add `Windows11` to `AppTheme` sealed class** and make it the ONLY returned theme (hardcode)
2. **Create new layout XMLs**: `taskbar_11.xml`, `start_menu_11.xml`, `windows_dialog_content_11.xml`, `windows_explorer_11.xml`, `taskbar_button_11.xml`
3. **Create new drawables** for Win11 look (rounded corners, dark backgrounds, accent colors)
4. **Add Win11 color palette** to colors.xml
5. **Update ThemeManager** to always return Win11 resources
6. **Add accent color system** to SharedPreferences + ThemeManager
7. **Smooth animations** — spring animations, shared element transitions, smooth scrolling

## FILES TO CREATE
- [x] `res/layout/taskbar_11.xml` — centered icons, search, rounded
- [x] `res/layout/start_menu_11.xml` — Win11 centered start with pinned + recommended
- [x] `res/layout/windows_dialog_content_11.xml` — rounded title bar, dark or light
- [x] `res/layout/windows_explorer_11.xml` — sidebar, breadcrumbs
- [x] `res/layout/taskbar_button_11.xml`
- [x] `res/drawable/win11_*` — all new drawables
- [x] `res/values/colors.xml` additions for Win11 palette

## FILES TO MODIFY
- [x] `ThemeManager.kt` — add Windows11 theme, update all resource mappings
- [x] `WindowsDialog.kt` — Win11 title bar behavior, rounded corners, smooth animations
- [x] `FloatingWindowManager.kt` — smooth open/close animations
- [x] `MainActivity.kt` — force Win11 theme, update references
- [x] `themes.xml` — add Win11 theme style
- [x] `styles.xml` — Win11 button styles
- [x] `colors.xml` — Win11 dark palette

## SMOOTHNESS IMPROVEMENTS
- Spring animations for window open/close/minimize
- Smoother drag with velocity tracking
- Hardware-accelerated rendering hints
- Reduced overdraw in layouts
- RecyclerView optimization (setHasFixedSize, itemAnimator)

## PROGRESS
- [x] Phase 1: Colors + Drawables
- [x] Phase 2: Layouts (taskbar, start menu, dialog, explorer, taskbar button)
- [x] Phase 3: ThemeManager + AppTheme update
- [x] Phase 4: WindowsDialog smooth animations
- [x] Phase 5: FloatingWindowManager animation
- [x] Phase 6: MainActivity integration
- [x] Phase 7: Commit and push
