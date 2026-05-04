package rocks.gorjan.gokixp

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import rocks.gorjan.gokixp.theme.AppTheme
import rocks.gorjan.gokixp.theme.ThemeManager

/**
 * Manager for creating floating windows within the app's view hierarchy.
 * Windows are added to a container FrameLayout with proper z-ordering.
 */
class FloatingWindowManager(private val context: Context, private val container: FrameLayout) {
    private val activeWindows = mutableListOf<WindowsDialog>()
    private val themeManager = ThemeManager(context)

    fun showWindow(windowsDialog: WindowsDialog) {
        // Unfocus all existing windows
        activeWindows.forEach { it.setUnfocused() }

        // Setup the window for in-app display
        windowsDialog.setupFloatingWindow(this)

        // Add to container with full size
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        container.addView(windowsDialog, params)
        activeWindows.add(windowsDialog)

        // Set the new window as focused
        windowsDialog.setFocused()

        // Smooth scale + fade animation for Win11
        windowsDialog.alpha = 0f
        windowsDialog.scaleX = 0.95f
        windowsDialog.scaleY = 0.95f
        windowsDialog.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(200)
            .setInterpolator(android.view.animation.DecelerateInterpolator(1.5f))
            .start()
    }

    fun removeWindow(windowsDialog: WindowsDialog) {
        try {
            // Unregister from taskbar before removing
            windowsDialog.unregisterFromTaskbar()

            // Smooth scale + fade out animation
            windowsDialog.animate()
                .alpha(0f)
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(150)
                .setInterpolator(android.view.animation.AccelerateInterpolator(1.5f))
                .withEndAction {
                    try {
                        container.removeView(windowsDialog)
                        activeWindows.remove(windowsDialog)
                    } catch (e: Exception) {
                        // Window might already be removed
                    }
                }
                .start()
        } catch (e: Exception) {
            // Window might already be removed
            try {
                container.removeView(windowsDialog)
                activeWindows.remove(windowsDialog)
            } catch (_: Exception) {}
        }
    }

    fun removeAllWindows() {
        activeWindows.toList().forEach { removeWindow(it) }
    }

    fun getAllActiveWindows(): List<WindowsDialog> {
        return activeWindows.toList()
    }

    fun bringToFront(windowsDialog: WindowsDialog) {
        // Use bringToFront() which is more efficient and doesn't disrupt layout
        if (activeWindows.contains(windowsDialog)) {
            // Unfocus all windows
            activeWindows.forEach { it.setUnfocused() }

            windowsDialog.bringToFront()

            // Update internal list order
            activeWindows.remove(windowsDialog)
            activeWindows.add(windowsDialog)

            // Focus the window being brought to front
            windowsDialog.setFocused()

            // Request layout to apply z-order change
            container.invalidate()
        }
    }

    /**
     * Gets the front-most window (last in the list)
     */
    fun getFrontWindow(): WindowsDialog? {
        return activeWindows.lastOrNull()
    }

    /**
     * Closes the front-most window
     */
    fun closeFrontWindow(): Boolean {
        val frontWindow = getFrontWindow()
        if (frontWindow != null) {
            // Trigger the close listener before removing
            frontWindow.triggerCloseListener()
            removeWindow(frontWindow)
            return true
        }
        return false
    }

    /**
     * Finds a window by its identifier and brings it to front if found.
     * Returns true if a window with the identifier was found, false otherwise.
     */
    fun findAndFocusWindow(identifier: String): Boolean {
        val existingWindow = activeWindows.find { it.windowIdentifier == identifier }
        if (existingWindow != null) {
            // If minimized, restore it
            if (existingWindow.isMinimized()) {
                existingWindow.restore()
            } else {
                // Otherwise just bring to front
                bringToFront(existingWindow)
            }
            return true
        }
        return false
    }

    /**
     * Finds a window by its identifier without focusing it.
     * Returns the window if found, null otherwise.
     */
    fun findWindowByIdentifier(identifier: String): WindowsDialog? {
        return activeWindows.find { it.windowIdentifier == identifier }
    }
}
