import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

const VideoPage = () => {
  const navigate = useNavigate();
  const videoRef = useRef(null);
  const [videoCompleted, setVideoCompleted] = useState(false);
  const [hasStarted, setHasStarted] = useState(false);

  useEffect(() => {
    const video = videoRef.current;
    if (!video) {
      return;
    }

    const handleEnded = () => {
      setVideoCompleted(true);
    };

    const handlePlay = () => {
      setHasStarted(true);
    };

    video.addEventListener("ended", handleEnded);
    video.addEventListener("play", handlePlay);

    return () => {
      video.removeEventListener("ended", handleEnded);
      video.removeEventListener("play", handlePlay);
    };
  }, []);

  return (
    <div className="page">
      <div className="card">
        <h1>Factory Visit Safety Briefing</h1>
        <p className="subtitle">
          Please watch the full safety video before proceeding to the
          registration form.
        </p>
        <div className="video-wrapper">
          <video
            ref={videoRef}
            controls
            className="video-player"
            aria-label="Factory safety introduction video"
          >
            <source
              src="https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"
              type="video/mp4"
            />
            Your browser does not support the video tag.
          </video>
        </div>
        <div className="actions">
          <button
            className="primary"
            type="button"
            disabled={!videoCompleted}
            onClick={() => navigate("/register")}
          >
            Next
          </button>
          {!videoCompleted && (
            <span className="hint">
              {hasStarted
                ? "Finish the video to continue."
                : "Start the video to unlock the Next button."}
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default VideoPage;
