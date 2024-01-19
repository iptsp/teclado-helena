fn main() {
    if cfg!(target_os = "windows") {
        let mut res = winres::WindowsResource::new();
        res.set_icon("assets/favicon.ico")
            .set_language(0x0416)
            .compile()
            .unwrap();
    }
}
