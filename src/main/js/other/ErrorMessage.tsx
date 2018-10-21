import * as React from "react";

export interface ErrorMessageProps {
    error: {
        response?: {
            data?: string,
            status: number,
            headers: string,
        },
        request?: object,
        message?: string,
    } | null;
}

export default function ErrorMessage({ error }: ErrorMessageProps): JSX.Element {
  if (error === null) {
    return <div id="errorNotPresent" />;
  }
  const { response, request } = error;
  if (response) {
    return (
      <div className="errorMessage" id="errorResponse">
        <h2>
          {response.status.toString()}
          {" "}
          {response.data !== undefined && response.data.toString()}
        </h2>
      </div>
    );
  }
  if (request) {
    return (
      <div className="errorMessage" id="errorRequest">
        <h2>
          No response
        </h2>
      </div>
    );
  }
  return (
    <div className="errorMessage" id="errorMessage">
      <h2>
        Error with request
      </h2>
    </div>
  );
}
