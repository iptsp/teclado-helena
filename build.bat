setlocal
cd %~dp0
cd client
call npm install
call npm run build
cd ..
cargo build --release
cd target\release
wix build ..\..\assets\Package.wxs -o teclado-helena.msi
