package com.didichuxing.doraemonkit.kit.mc.all.hook

import android.view.View
import android.view.accessibility.AccessibilityEvent
import com.didichuxing.doraemonkit.kit.core.DoKitManager
import com.didichuxing.doraemonkit.constant.WSMode
import com.didichuxing.doraemonkit.kit.core.DokitFrameLayout
import com.didichuxing.doraemonkit.kit.mc.ability.monitor.McAccessibilityEventMonitor
import com.didichuxing.doraemonkit.mc.R
import com.didichuxing.doraemonkit.util.LogHelper
import de.robv.android.xposed.XC_MethodHook

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/11/30-19:55
 * 描    述：View# onInitializeAccessibility hook
 * wiki:https://blog.csdn.net/u011391629/article/details/83343312
 * http://lionoggo.com/2018/03/22/%E6%B7%B1%E5%85%A5Android%E8%BE%85%E5%8A%A9%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E4%B8%8E%E8%AE%BE%E8%AE%A1/
 * https://blog.csdn.net/omnispace/article/details/70598515
 * 修订历史：sendAccessibilityEventUncheckedInternal 中会被调用
 * ================================================
 *
 */
class ViewOnInitializeAccessibilityEventHook : XC_MethodHook() {

    companion object {
        const val TAG = "ViewOnInitializeAccessibilityEventHook"
    }

    /**
     * https://developer.android.google.cn/reference/android/view/accessibility/AccessibilityEvent
     */
    override fun afterHookedMethod(param: MethodHookParam?) {
        super.afterHookedMethod(param)
        if (DoKitManager.WS_MODE != WSMode.HOST) {
            return
        }
        param?.let {
            val view = it.thisObject as View
            val accessibilityEvent = it.args[0] as AccessibilityEvent
            LogHelper.i(TAG, "view==>${view},accessibilityEvent=${accessibilityEvent},eventType===${Integer.toHexString(accessibilityEvent.eventType)}")

            if (view is DokitFrameLayout && accessibilityEvent.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
                return
            }
            if (view.id == R.id.dokit_mode_switch_btn) {
                return
            }
            McAccessibilityEventMonitor.onAccessibilityEvent(view, accessibilityEvent)
        }
    }


}