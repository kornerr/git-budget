const WASM_FILE = "hwcli.wasm";

export async function initWasm() {
    let instance;
    let storedCallback;

    const importObject = {
        wasi_snapshot_preview1: {
            args_get: () => 0,
            args_sizes_get: () => 0,
            environ_get: () => 0,
            environ_sizes_get: () => 0,
            fd_close: () => 0,
            fd_fdstat_get: () => 0,
            fd_prestat_get: () => 8,
            fd_prestat_dir_name: () => 8,
            fd_read: () => 0,
            fd_seek: () => 0,
            fd_write: () => 0,
            path_open: () => 8,
            proc_exit: () => {},
            random_get: () => 0,
        },
        env: {
            jsCallback: () => {
                if (!storedCallback) return;
                const e = instance.exports;
                storedCallback({
                    get didLaunch() { return !!e.DataContext_didLaunch(); },
                    get selectedId() { return e.DataContext_selectedId(); },
                    get url() { return readStr(e.DataContext_url()); },
                    get recentField() { return readStr(e.DataContext_recentField()); },
                });
            },
        },
    };

    const result = await WebAssembly.instantiateStreaming(fetch(WASM_FILE), importObject);
    instance = result.instance;
    const mem = () => new Uint8Array(instance.exports.memory.buffer);

    const copyStrBuf = (b, buf, enc, off) => {
        for (let i = 0; i < enc.length; i++) b[buf + off + i] = enc[i];
    };

    const readStr = (p) => {
        const b = mem(); let e = p;
        while (b[e]) e++;
        return new TextDecoder().decode(b.slice(p, e));
    };

    const writeStr = (v, fn) => {
        const enc = new TextEncoder().encode(v + "\0");
        const buf = instance.exports.strBuf();
        const b = mem();
        copyStrBuf(b, buf, enc, 0);
        fn(buf);
    };

    const sendAny = (key, value) => {
        const enc = new TextEncoder().encode(key + "\0");
        const keyPtr = instance.exports.strBuf();
        const valOffset = 64;
        const strValPtr = keyPtr + valOffset;
        const b = mem();
        copyStrBuf(b, keyPtr, enc, 0);
        if (typeof value === "string") {
            const val = new TextEncoder().encode(value + "\0");
            copyStrBuf(b, keyPtr, val, valOffset);
            instance.exports.sendAnyString(keyPtr, strValPtr);
        } else if (typeof value === "number") {
            instance.exports.sendAnyInt(keyPtr, value);
        } else if (typeof value === "boolean") {
            instance.exports.sendAnyBool(keyPtr, value);
        }

    };

    return {
        exports: instance.exports,
        readStr,
        writeStr,
        sendAny,
        registerCallback: (fn) => { storedCallback = fn; instance.exports.registerCallback(); },
    };
}
