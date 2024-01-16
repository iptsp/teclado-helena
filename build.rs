use npm_rs::*;

fn main() {
    let exit_status = NpmEnv::default()
        .with_node_env(&NodeEnv::from_cargo_profile().unwrap_or_default())
        .with_env("FOO", "bar")
        .set_path("./client")
        .init_env()
        .install(None)
        .run("build")
        .exec()
        .unwrap();
    println!("exit status: {}", exit_status);
}
