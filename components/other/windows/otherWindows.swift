import WinSDK

func otherSetupWindowIcon(_ iconId: Int) {
    // Only visit windows owned by this UI thread, never another application's window
    EnumThreadWindows(GetCurrentThreadId(), { wnd, iconId in
        let resource = UnsafePointer<WCHAR>(bitPattern: Int(iconId))
        let module = GetModuleHandleW(nil)

        // Small icon
        if let small = LoadImageW(
            module,
            resource,
            UINT(IMAGE_ICON),
            GetSystemMetrics(SM_CXSMICON),
            GetSystemMetrics(SM_CYSMICON),
            UINT(LR_SHARED)
        ) {
            SendMessageW(wnd, UINT(WM_SETICON), WPARAM(ICON_SMALL), LPARAM(Int(bitPattern: small)))
        }

        // Large icon
        if let large = LoadImageW(
            module,
            resource,
            UINT(IMAGE_ICON),
            GetSystemMetrics(SM_CXICON),
            GetSystemMetrics(SM_CYICON),
            UINT(LR_SHARED)
        ) {
            SendMessageW(wnd, UINT(WM_SETICON), WPARAM(ICON_BIG), LPARAM(Int(bitPattern: large)))
        }

        return true
    }, LPARAM(iconId))
}
