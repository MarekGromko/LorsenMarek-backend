package edu.lorsenmarek.backend.exception;

/**
 * {@link RuntimeException} thrown after trying to rate a media by a user that hasn't yet watched it
 */
public class RatingUnwatchedMediaException extends RuntimeException{
}
