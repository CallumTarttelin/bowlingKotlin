import React from 'react';
import PropTypes from 'prop-types';

export default function Error({ error }) {
  const { response, request } = error;
  if (error === null) {
    return <div id="errorNotPresent" />;
  }
  if (error.response) {
    return (
      <div className="errorMessage" id="errorResponse">
        <h2>
          {response.status}
          {' '}
          {response.data}
        </h2>
      </div>
    );
  } if (request) {
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

Error.propTypes = {
  error: PropTypes.shape({
    response: PropTypes.shape({
      data: PropTypes.string,
      status: PropTypes.number,
      headers: PropTypes.string,
    }),
    request: PropTypes.object,
    message: PropTypes.string,
  }).isRequired,
};
