![Magma](https://img.hexeption.co.uk/magma.png)

![](https://img.shields.io/badge/Minecraft%20Forge-1.12.2%20--%202854-orange.svg?style=for-the-badge) [![](https://img.shields.io/jenkins/build/https/ci.hexeption.co.uk/job/Magma-Main/job/master?label=CI&style=for-the-badge)](https://ci.hexeption.co.uk)
![](https://bstats.org/signatures/bukkit/magma.svg)

## About

Magma is the next generation of hybrid minecraft server softwares.

Magma is based on Forge and Paper, meaning it can run both Craftbukkit/Spigot/Paper plugins and Forge mods.

We hope to eliminate all issues with craftbukkit forge servers. In the end, we envision a seamless, low lag Magma experience with support for newer 1.12+ versions of Minecraft.

## Deployment

### Installation

1. Download the recommended builds from the [**Releases** section](https://github.com/magmafoundation/Magma/releases)
   1. Download Beta builds from the [**CI**](https://ci.hexeption.co.uk/job/Magma-Main/job/master/)
2. Make a new directory for the server
3. Move the jar that you previously downloaded to the new directory
4. Run the jar

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

## Contributing

If you wish to inspect Magma, submit PRs, or otherwise work with Magma itself, you're in the right place!.

Please read the [CONTRIBUTING.md](https://github.com/magmafoundation/Magma/blob/master/CONTRIBUTING.md) to see how to contribute, setup, and run.

## Chat

You are welcome to visit Magma's Discord server [here](https://discord.gg/6rkqngA).

## Unstable/Test builds

For unstable/test builds you can check the [__CI__](https://ci.hexeption.co.uk/job/Magma-Main/job/master/)

## Partners
<a href="https://aternos.org/en/"><img src="https://design.aternos.org/dl/logotype-horizontal-blue.png" width="200"></a>
<a href="https://songoda.com/"><img src="https://cdn2.songoda.com/branding/logo.svg" width="200"></a>
