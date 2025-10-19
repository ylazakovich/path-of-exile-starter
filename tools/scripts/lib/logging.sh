#!/bin/bash
# ============================================================================
#  Common logging utilities for Bash scripts
#  Usage: source "$(dirname "$0")/../lib/logging.sh"
# ============================================================================

# Colored log output with prefixes
info() { printf "\033[1;36mℹ️  %s\033[0m\n" "$*"; }
warning() { printf "\033[1;33m⚠️  %s\033[0m\n" "$*"; }
error() { printf "\033[1;31m❌ %s\033[0m\n" "$*"; }

# Optional: fatal shortcut
fatal() {
  error "$@"
  exit 1
}
