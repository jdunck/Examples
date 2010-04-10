introduced in 1.5
content comes from content provider
participants have the ACTION_CREATE_LIVE_FOLDER intent filter in their manifest
live folders contain these components:
- folder name
- folder icon
- display mode (grid or list)
- content provider uri

steps:
set up intent filter
handle action in onCreate