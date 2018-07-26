import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/es/Button/Button';

export default function Header({ title, back, backLocation }) {
  return (
    <header className="App-header">
      {backLocation !== ''
        && (
        <Link className="back" to={backLocation}>
          <Button variant="raised">
            {back}
          </Button>
        </Link>
        )
      }
      <h1 className="App-title">
        {title}
      </h1>
    </header>
  );
}

Header.propTypes = {
  title: PropTypes.string.isRequired,
  back: PropTypes.string,
  backLocation: PropTypes.string,
};

Header.defaultProps = {
  back: 'Back',
  backLocation: '',
};
