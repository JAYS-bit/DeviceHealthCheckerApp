
# Android Diagnostic App

## Overview

This Android application is designed to conduct comprehensive device health checks in a single, guided flow. The app performs various tests, including camera detection, microphone testing, rooted status check, Bluetooth functionality test, and sensor status verification. The user is guided through each test with clear instructions, and the UI is updated in real-time with the test results.

## Features

1. **User Interface and Guided Flow:**
   - Intuitive and guided user interface.
   - Designed layout and user interactions for a seamless flow.
   - Clear instructions for each test, prompting user actions.

2. **Device Health Checks:**
   - Continuous checks for:
     - Camera detection using Google ML Kit.
     - Verification of both microphones with user instructions.
     - Rooted or non-rooted status check.
     - Bluetooth functionality test.
     - Status verification of various sensors (e.g., accelerometer, gyroscope, GPS).

3. **Continuous Health Checks:**
   - Mechanism for uninterrupted flow through each test.
   - UI updates with test names and instructions during checks.

4. **Real-time UI Updates:**
   - UI updates in real-time with pass/fail status of each health check.
   - Provides real-time instructions to the user.

5. **Result Output:**
   - Two options provided at the end:
     - Option 1: Send data to Firebase.
     - Option 2: Generate a PDF report.

## Implementation Details

- Developed in Java for Android.
- Utilized Google ML Kit for camera detection.
- Real-time UI updates implemented for clear user feedback.
- Firebase integration for data storage and retrieval.
- Graceful error handling with helpful instructions to the user.

## Submission
 **Code Repository:** [GitHub Link]([https://github.com/JAYS-bit/DeviceHealthCheckerApp])
