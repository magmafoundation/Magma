<div align="center">
<img src="https://i.imgur.com/zTCTCWG.png" alt="Magma logo" align="middle"></img>

![](https://img.shields.io/badge/Minecraft%20Forge-1.12.2%20--%202854-orange.svg?style=for-the-badge)
[![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.hexeption.dev%2Fjob%2FMagma%2520Foundation%2Fjob%2FMagma%2Fjob%2Fmaster%2F&style=for-the-badge)](https://ci.hexeption.dev)

![](https://bstats.org/signatures/bukkit/magma.svg)
</div>

## About

Magma is the next generation of hybrid minecraft server softwares.

Magma is based on Forge and Paper, meaning it can run both Craftbukkit/Spigot/Paper plugins and Forge mods.

We hope to eliminate all issues with craftbukkit forge servers. In the end, we envision a seamless, low lag Magma experience with support for newer 1.12+ versions of Minecraft.

## Big Paper Update :fireworks:

Magma at the moment ain't using all of the API/Patches with this update we will have all features of Paper and more optimizations and including Timings v2

We can't wait to have this finished and shared with all for a better/faster server software

We have Dev Build channel in the discord for this update so you can come and test it out and find bugs for us to fix before release

All work is being done on the [feature/full-paper-support](https://github.com/magmafoundation/Magma/tree/feature/full-paper-support) branch if you would like to check it out

## 1.15

Magma 1.15.x is in beta! Learn more [here](https://github.com/magmafoundation/Magma-1.15.x).

## 1.16

Magma 1.16.x is in development, but a usable release is not available yet. You can still view the source code [here](https://github.com/magmafoundation/Magma-1.16.x).

## Deployment

### Installation

1. Download the recommended builds from the [**Releases** section](https://github.com/magmafoundation/Magma/releases) (Don't use the "-installer" version, it's broken right now)
   1. Download Beta builds from the [**CI**](https://ci.hexeption.dev/job/Magma%20Foundation/job/Magma/job/master/)
2. Make a new directory for the server
3. Move the jar that you previously downloaded to the new directory
4. Run the jar with cmd, going to your directory and entering `java -jar Magma-[version here]-server.jar`. Change [version-here] to your Magma version number.

### Building the sources

- Clone the Project
  - You can use Git GUI (like GitHub Desktop/GitKraken) or clone using the terminal using:
    - `git clone https://github.com/MagmaFoundation/Magma`
  - Next, clone the submodules using:
    - `git submodule update --init --recursive`
- Building
  - First you want to run the build command
    - `./gradlew launch4j`
  - Now go and get a drink this may take some time
  - Navigate to `build/distributions` directory of the compiled source code
  - Copy the Jar to a new server directory
  
### Plugin Development

- Clone the Project
  - You can use Git GUI (like GitHub Desktop/GitKraken) or clone using the terminal using:
    - `git clone https://github.com/MagmaFoundation/Magma`
  - Next, clone the submodules using:
    - `git submodule update --init --recursive`
- Building Jar
  - First you want to run the plugin gen command
    - `./gradlew genPluginJar`
  - Now go and get a drink this may take some time
  - Navigate to `build/distributions` directory
  - You should have a jar like `Magma-xxxxxx-plugin.jar`
  - Now create a plugin with that jar.
  


## Contributing

If you wish to inspect Magma, submit PRs, or otherwise work with Magma itself, you're in the right place!.

Please read the [CONTRIBUTING.md](https://github.com/magmafoundation/Magma/blob/master/CONTRIBUTING.md) to see how to contribute, setup, and run.

## Chat

You are welcome to visit Magma's Discord server [here](https://discord.gg/6rkqngA).

## Unstable/Test builds

For unstable/test builds you can check the [__CI__](https://ci.hexeption.dev/job/Magma%20Foundation/job/Magma/job/master/)

## Partners
<a href="https://aternos.org/en/"><img src="https://company.aternos.org/img/logotype-blue.svg" width="200"></a>
<a href="https://songoda.com/"><img src="https://cdn2.songoda.com/branding/logo.png" width="200"></a>
<a href="https://serverjars.com/"><img src="https://serverjars.com/assets/img/logo_white.svg" width="200"></a>
<a href="https://craftycontrol.com/"><img src="https://i.imgur.com/243oDOX.png" width="100" height="100"></a>

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler/index.jsp) and [.NET](https://www.yourkit.com/.net/profiler/index.jsp) application profilers.

