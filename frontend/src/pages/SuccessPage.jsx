import { Link, useLocation } from "react-router-dom";

const SuccessPage = () => {
  const { state } = useLocation();
  const message = state?.message || "Registration complete.";
  const qrCodeBase64 = state?.qrCodeBase64 || "";
  const emailSent = state?.emailSent || false;
  const email = state?.email || "";

  return (
    <div className="page">
      <div className="card center">
        <h1>Registration Complete</h1>
        <p className="subtitle">{message}</p>
        {email && (
          <p className="subtitle">
            {emailSent
              ? `A copy has been sent to ${email}.`
              : `Email delivery is not configured. Showing the QR code here.`}
          </p>
        )}
        {qrCodeBase64 && (
          <div className="qr-preview">
            <img
              src={`data:image/png;base64,${qrCodeBase64}`}
              alt="QR code for your visit"
            />
          </div>
        )}
        <Link className="primary link-button" to="/">
          Back to Video
        </Link>
      </div>
    </div>
  );
};

export default SuccessPage;
