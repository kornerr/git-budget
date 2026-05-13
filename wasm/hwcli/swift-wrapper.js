const wasi = { wasi_snapshot_preview1: {
  args_get: () => 0, args_sizes_get: () => 0,
  environ_get: () => 0, environ_sizes_get: () => 0,
  fd_close: () => 0, fd_fdstat_get: () => 0,
  fd_prestat_get: () => 8, fd_prestat_dir_name: () => 8,
  fd_read: () => 0, fd_seek: () => 0, fd_write: () => 0,
  path_open: () => 8, proc_exit: () => {},
  random_get: () => 0,
}};

export async function initWasm() {
  const { instance } = await WebAssembly.instantiateStreaming(fetch("hwcli.wasm"), wasi);
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

  return { exports: instance.exports, readStr, writeStr };
}
