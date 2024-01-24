setlocal
cd %~dp0
cd client
call npm install
call npm run build
cd ..
cargo run
