# uhttp

This is a native CLI tool created using [Clojure][clojure] and
[GraalVM][graalvm]. Its main purpose is to prove that [unixsocket-http][] can
be compiled using GraalVM.

[clojure]: https://clojure.org/
[graalvm]: https://www.graalvm.org/
[unixsocket-http]: https://github.com/into-docker/unixsocket-http

## Build

Make sure you have [native-image][] installed and run:

```sh
lein uberjar
$GRAALVM_HOME/bin/native-image -jar target/uhttp.jar -H:Name=uhttp
```

Afterwards, you should have `uhttp` binary available. Note that all native-image
configuration is part of the Uberjar.

## Run

The native image can be run on any platform that provides [glibc][] and is
supported by [junixsocket][] (the underlying Java/Native bridge). You also need
to ensure that the directory identified by the property `java.io.tmpdir` exists
and is writeable.

### `uhttp <socket> <path>`

This will send an HTTP request over the given UNIX Domain Socket. For example, to ping
the Docker daemon:

```
$ ./uhttp unix:///var/run/docker.sock /_ping
{:status 200, :headers {"api-version" "1.40", "server" "Docker/19.03.2 (linux)", "content-type" "text/plain; charset=utf-8", "content-length" "2", "docker-experimental" "false", "pragma" "no-cache", "date" "Sat, 11 Apr 2020 15:47:35 GMT", "ostype" "linux", "cache-control" "no-cache, no-store, must-revalidate"}, :body "OK"}
```

### `uhttp`

This will run a small diagnostics program, attempting to load the native library
using a mechanism very close to what [junixsocket][] actually does:

```
$ ./uhttp
Native Library:  libjunixsocket-native-2.3.2.so
  Architecture:  amd64
  OS:            Linux
  Path:          /lib/amd64-Linux-clang/jni/libjunixsocket-native-2.3.2.so
  Temp Dir:      /var/tmp/
Loading native library ...
  Copying to /var/tmp/libtmp2171414089132613831libjunixsocket-native-2.3.2.so ...
  Loading /var/tmp/libtmp2171414089132613831libjunixsocket-native-2.3.2.so ...
  OK!
```

[native-image]: https://www.graalvm.org/docs/reference-manual/native-image/
[junixsocket]: https://github.com/kohlschutter/junixsocket
[glibc]: https://www.gnu.org/software/libc/

## License

```
MIT License

Copyright (c) 2020 Yannick Scherer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
