# Changelog

All notable changes to `pillow-android-sdk` will be documented in this file.

## [Unreleased]

## [0.1.5] - 2026-04-28

- Treat the local SQLite installation row as the source of truth for fresh-install detection. Cloud-backup restores now preserve the audience installation_id instead of rotating it.
- Report the Pillow SDK version in audience telemetry instead of the host app's `versionName`.

## [0.1.4] - 2026-04-17

- Add `presentLaunchStudyIfAvailable(activity:delegate:)` and `onReadyToPresentStudy(activity:)` to support audience-targeted launch studies.

## [0.1.3] - 2026-04-10

- Fix study webview memory retention after dismiss.

## [0.1.1] - 2026-04-10

- Initial Android distribution scaffold.
