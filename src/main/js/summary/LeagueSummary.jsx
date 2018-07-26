import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function LeagueSummary({ children, id }) {
  return (
    <li className={children.replace(/\s+/g, '-').toLowerCase()}>
      <Link to={`/league/${id.toString()}`}>
        <div className="LeagueSummary">
          {children}
        </div>
      </Link>
    </li>
  );
}

LeagueSummary.propTypes = {
  children: PropTypes.string.isRequired,
  id: PropTypes.number.isRequired,
};
