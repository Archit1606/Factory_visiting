import { Routes, Route, Navigate } from "react-router-dom";
import VideoPage from "./pages/VideoPage";
import RegistrationPage from "./pages/RegistrationPage";
import SuccessPage from "./pages/SuccessPage";

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<VideoPage />} />
      <Route path="/register" element={<RegistrationPage />} />
      <Route path="/success" element={<SuccessPage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
};

export default App;
