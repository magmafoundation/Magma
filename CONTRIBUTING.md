# How to contribute

Please take a moment to read this document in order to make the contribution process easy and effective.

## Using the issue tracker

use the issues tracker for:

* [bug reports](#bug-reports)
* [feature requests](#feature-requests)
* [submitting pull requests](#pull-requests)

## Bug reports

A bug is either a _demonstrable problem_ that is caused in Magma failing to provide the expected feature or indicate missing, unclear, or misleading documentation. Good bug reports are extremely helpful - thank you!
A Guidelines for bug reports:

1. **Use the GitHub issue search** &mdash; check if the issue has already been reported.

2. **Check if the issue has been fixed** &mdash; try to reproduce it using the `master` branch in the repository.

3. **Isolate and report the problem** &mdash; ideally create a reduced test case.

Please try to be as detailed as possible in your report. Include information about your Operating System, as well as your `java -version`. Please provide steps to reproduce the issue as well as the outcome you were expecting! All these details will help developers to fix any potential bugs.


## Feature requests

Feature requests are welcome and should be discussed on issue tracker. But take a moment to find out whether your idea fits with the scope and aims of the project. It's up to *you* to make a strong case to convince the community of the merits of this feature. Please provide as much detail and context as possible.

## Pull requests

Good pull requests - patches, improvements, new features - are a fantastic help. They should remain focused in scope and avoid containing unrelated commits.

**IMPORTANT**: By submitting a patch, you agree that your work will be licensed under the license used by the project.

If you have any large pull request in mind (e.g. implementing features, refactoring code, etc), **please ask first** otherwise you risk spending a lot of time working on something that the project's developers might not want to merge into the project.

Please adhere to the coding conventions in the project (indentation, accurate comments, etc.).

## How to setup and build a local version of Magma

### Prerequisites

- [JDK8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Intellij Community/Ultimate](https://www.jetbrains.com/idea/)

### Setting up
- Fork the project.

- You can use GitHub Desktop/GitKraken or clone using the terminal 

  - `git clone https://github.com/YOUR_GITHUB_USER/Magma.git`
  - Next you are gonna want to clone the submodule
    - `git submodule update --init --recursive` 

- Next open Intellij and import the project.

  - Wait for the project to be synced 

- Now open the gradle tab and open the Magma Tasks and run the `forgegradle/setup task`.

  - This may take a few minutes go get a drink or make food.

- Once it has finished the setup task reimport gradle using the [refresh button](https://img.hexeption.co.uk/0SuC5IkXt1.png) on the gradle tab 

- Next [import](https://img.hexeption.co.uk/GNLINGNJtD.png) the build.gradle from the [Magma/projects](https://img.hexeption.co.uk/JfcA58TK80.png) folder 

- Once imported refresh the gradle once more.

Only edit the code in the Projects/Magma Folder and the main SRC folder. 

### Running

- First open the Edit Configurations and click the [add button](https://img.hexeption.co.uk/7nBlAq7iui.png) for application.
- Copy these [Settings](https://img.hexeption.co.uk/JbD9b9EAQ7.png) 
- Finally click the run button 

### Building

- First make sure you ran the genMagmaPatchs before running the `Magma/Tasks/launch4j/launch4j`
- Your build should be in the `build/distributions`

### Running Tests


