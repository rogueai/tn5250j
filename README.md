# TN5250J
A 5250 terminal emulator for the IBM i (AS/400) written in Java.

Documentation is available at: [tn5250j.github.io](https://tn5250j.github.io/)

[![Build Status](https://travis-ci.org/tn5250j/tn5250j.svg?branch=travis)](https://travis-ci.org/tn5250j/tn5250j)

## About this fork
This fork aims at implementing GDDM support for tn5250j.
Currently, the only drawing order code that is somewhat functional is Draw Polyline, along with related non-drawing
orders such End of Data, End of Graphics, etc.

The project is still heavily work in progress, so you might expect:
- missing or misbehaving orders
- wrong behaviour
- glitches
- regressions in existing functionality
- lots of FIXME and HACK comments :)

Until I find a suitable way to unit test GDDM scenarios, tests are mostly manual and performed on a 9406-170 V4R2.

## History

This project was created because there was no terminal emulator for Linux with features like continued edit fields, gui windows, cursor progression fields, etc.

It was then open sourced to give something back to all those hackers and code churners that work so hard to provide the Linux and Open Source communities with quality work and software.

The original developer wanted it to work on different operating systems, thus Java giving the “J” at the end.

## Hosting

The project was previous hosted at [sourceforge.net](https://sourceforge.net/projects/tn5250j/). But since 2016 has been migrated to GitHub.
