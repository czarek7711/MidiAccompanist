package czys.midi.accompanist.player;

enum PlaybackState {
    NORMAL,
    FORWARDING,
    PAUSED // not actually paused, tempo is set to minimum value
}
