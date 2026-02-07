import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

const initialState = {
  fullName: "",
  email: "",
  phone: "",
  company: "",
  purpose: ""
};

const RegistrationPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialState);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState("");

  const isFormDirty = useMemo(
    () => Object.values(formData).some((value) => value.trim().length > 0),
    [formData]
  );

  const validate = (values) => {
    const nextErrors = {};

    if (!values.fullName.trim()) {
      nextErrors.fullName = "Full Name is required.";
    }

    if (!values.email.trim()) {
      nextErrors.email = "Email Address is required.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.email)) {
      nextErrors.email = "Enter a valid email address.";
    }

    if (!values.phone.trim()) {
      nextErrors.phone = "Phone Number is required.";
    } else if (values.phone.replace(/\D/g, "").length < 10) {
      nextErrors.phone = "Phone Number must be at least 10 digits.";
    }

    if (!values.company.trim()) {
      nextErrors.company = "Company Name is required.";
    }

    if (!values.purpose.trim()) {
      nextErrors.purpose = "Purpose of Visit is required.";
    }

    return nextErrors;
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitError("");

    const validationErrors = validate(formData);
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) {
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await api.post("/api/visitors/register", {
        fullName: formData.fullName.trim(),
        email: formData.email.trim(),
        phone: formData.phone.trim(),
        companyName: formData.company.trim(),
        purposeOfVisit: formData.purpose.trim()
      });
      navigate("/success", {
        state: {
          qrCodeBase64: response?.data?.qrCodeBase64 || "",
          message: response?.data?.message || "Registration complete.",
          emailSent: response?.data?.emailSent || false,
          email: formData.email.trim()
        }
      });
    } catch (error) {
      setSubmitError(
        error?.response?.data?.message ||
          "We could not complete the registration. Please try again."
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="page">
      <div className="card">
        <h1>POC Registration Form</h1>
        <p className="subtitle">Provide your visit details below.</p>
        <form className="form" onSubmit={handleSubmit} noValidate>
          <div className="field">
            <label htmlFor="fullName">Full Name</label>
            <input
              id="fullName"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              placeholder="Enter your full name"
              required
            />
            {errors.fullName && <span className="error">{errors.fullName}</span>}
          </div>

          <div className="field">
            <label htmlFor="email">Email Address</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@company.com"
              required
            />
            {errors.email && <span className="error">{errors.email}</span>}
          </div>

          <div className="field">
            <label htmlFor="phone">Phone Number</label>
            <input
              id="phone"
              name="phone"
              type="tel"
              value={formData.phone}
              onChange={handleChange}
              placeholder="+1 555 010 1234"
              required
            />
            {errors.phone && <span className="error">{errors.phone}</span>}
          </div>

          <div className="field">
            <label htmlFor="company">Company Name</label>
            <input
              id="company"
              name="company"
              value={formData.company}
              onChange={handleChange}
              placeholder="Company name"
              required
            />
            {errors.company && <span className="error">{errors.company}</span>}
          </div>

          <div className="field">
            <label htmlFor="purpose">Purpose of Visit</label>
            <textarea
              id="purpose"
              name="purpose"
              value={formData.purpose}
              onChange={handleChange}
              placeholder="Purpose of your visit"
              rows={3}
              required
            />
            {errors.purpose && <span className="error">{errors.purpose}</span>}
          </div>

          {submitError && <div className="error-banner">{submitError}</div>}

          <div className="actions">
            <button className="primary" type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Submitting..." : "Submit"}
            </button>
            {isFormDirty && (
              <button
                className="secondary"
                type="button"
                onClick={() => {
                  setFormData(initialState);
                  setErrors({});
                  setSubmitError("");
                }}
              >
                Reset
              </button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegistrationPage;
