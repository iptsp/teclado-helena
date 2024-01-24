setlocal
cd %~dp0
rmdir /s /q docs
rmdir /s /q target/doc
cargo doc --no-deps
robocopy target/doc docs/server /s /e
echo ^<meta http-equiv="refresh" content="0; url=server/teclado_helena/index.html"^> > docs/index.html