#!/bin/bash
# ============================================================================
#  Common logging utilities for Bash scripts
#  Usage: source "$(dirname "$0")/../lib/logging.sh"
# ============================================================================

# Colored log output with prefixes
info()    { echo -e "\033[1;34mInfo:    $*\033[0m"; }
warning() { echo -e "\033[1;33mWarning: $*\033[0m"; }
error()   { echo -e "\033[1;31mError:   $*\033[0m"; }

# Optional: fatal shortcut
fatal() {
  error "$@"
  exit 1
}
