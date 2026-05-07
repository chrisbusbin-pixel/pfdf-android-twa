# Prop Firm Deal Finder — Android (Trusted Web Activity)

The official Android app for [Prop Firm Deal Finder (PFDF)](https://propfirmdealfinder.com) — packaged as a Trusted Web Activity (TWA) wrapping the responsive PFDF web experience.

## What is PFDF?

PFDF is the largest aggregator of proprietary trading firm discounts, reviews, and deals. We track 20+ futures, forex, and multi-asset prop firms with verified discount codes updated daily.

**Universal discount code: `PFDF`** — works at every partner firm.

### Key features

- **Live deals** — Real-time discount tracking across 20+ firms
- **Smart finder** — Filter by account size, drawdown type, and asset class
- **Side-by-side comparison** — 15+ data points per firm
- **Fee calculator** — Calculate exact checkout price including discount
- **Daily market prep** — Futures market signals updated daily
- **One-tap code copy** — Every discount code copies with a single tap

## Where to get PFDF

- 🌐 **Web:** [propfirmdealfinder.com](https://propfirmdealfinder.com)
- 📱 **iOS App Store:** [apps.apple.com/us/app/prop-firm-deal-finder/id6758235452](https://apps.apple.com/us/app/prop-firm-deal-finder/id6758235452)
- 🪟 **Microsoft Store:** [apps.microsoft.com/detail/9PJD0XN2V58Q](https://apps.microsoft.com/detail/9PJD0XN2V58Q)
- 🧩 **Chrome Extension:** [chromewebstore.google.com](https://chromewebstore.google.com/detail/prop-firm-deal-finder/bhciijhpmnildmgnjbbbmdhhdebfhpfa)
- 🤖 **MCP Server (for Claude/ChatGPT):** [npmjs.com/package/propfirmdealfinder-mcp-server](https://www.npmjs.com/package/propfirmdealfinder-mcp-server)

## About this repo

This is the Android wrapper that creates a TWA (Trusted Web Activity) of the PFDF web app, distributable via the Google Play Store. TWAs let users get a native-feeling Android experience while the underlying app remains a single codebase served from propfirmdealfinder.com.

### Build

```bash
./gradlew assembleRelease
```

The output APK/AAB will be in `app/build/outputs/`.

### Configuration

- Web manifest, Digital Asset Links, and TWA config are inside `app/src/main/`.
- Make sure `assetlinks.json` is hosted at `propfirmdealfinder.com/.well-known/assetlinks.json` for the TWA to launch without the URL bar.

### Resources

- [Trusted Web Activity overview](https://developers.google.com/web/android/trusted-web-activity)
- [Bubblewrap](https://github.com/GoogleChromeLabs/bubblewrap) — the TWA tooling

## License

© 2026 Chris Busbin. All rights reserved.
