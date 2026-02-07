import { Link } from "react-router-dom";

const SuccessPage = () => {
  return (
    <div className="page">
      <div className="card center">
        <h1>Registration Complete</h1>
        <p className="subtitle">
          Your QR Code has been generated and sent to your email.
        </p>
        <Link className="primary link-button" to="/">
          Back to Video
        </Link>
      </div>
    </div>
  );
};

export default SuccessPage;
