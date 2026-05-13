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
            jsCallback: () => { if (storedCallback) storedCallback(); },
        },
    };

    const result = await WebAssembly.instantiateStreaming(fetch(WASM_FILE), importObject);
    instance = result.instance;
    const mem = () => new Uint8Array(instance.exports.memory.buffer);

    const readStr = (p) => {
        const b = mem(); let e = p;
        while (b[e]) e++;
        return new TextDecoder().decode(b.slice(p, e));
    };

    const writeStr = (v, fn) => {
        const enc = new TextEncoder().encode(v + "\0");
        const buf = instance.exports.strBuf();
        const b = mem();
        for (let i = 0; i < enc.length; i++) b[buf + i] = enc[i];
        fn(buf);
    };

    const sendAny = (key, value) => {
        const enc = new TextEncoder().encode(key + "\0");
        const buf = instance.exports.strBuf();
        const b = mem();
        for (let i = 0; i < enc.length; i++) b[buf + i] = enc[i];
        if (typeof value === "string") {
            const encV = new TextEncoder().encode(value + "\0");
            for (let i = 0; i < encV.length; i++) b[buf + 64 + i] = encV[i];
            instance.exports.sendAnyString(buf, buf + 64);
        } else if (typeof value === "number") {
            instance.exports.sendAnyInt(buf, value);
        } else if (typeof value === "boolean") {
            instance.exports.sendAnyBool(buf, value);
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
