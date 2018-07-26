import React, { Component } from 'react';
import axios from 'axios';
import CircularProgress from '@material-ui/core/es/CircularProgress/CircularProgress';
import Button from '@material-ui/core/es/Button/Button';
import LeagueSummary from '../summary/LeagueSummary';
import Header from './Header';
import Error from './Error';

class LeagueList extends Component {
  constructor() {
    super();
    this.state = { status: 'Loading', error: {} };
    this.update = this.update.bind(this);
    this.update();
  }

  update() {
    axios.get('/api/league')
      .then((response) => {
        this.setState({ status: 'ok', leagues: response.data });
      })
      .catch((err) => {
        this.setState({ status: 'Error', error: err });
      });
  }

  render() {
    const { status, leagues, error } = this.state;
    switch (status) {
      case 'Loading': return (
        <div className="Loading">
          <CircularProgress color="primary" />
        </div>
      );
      case 'Error': return (
        <div className="Error">
          <Error error={error} />
          <Button variant="raised" color="primary" className="RefreshButton" onClick={this.refresh}>
            Refresh Leagues
          </Button>
        </div>
      );

      default: return (
        <div className="LeagueList">
          <Header title="Bowling Leagues!" />

          <div className="Leagues">
            <ul>
              {leagues.map(league => (
                <LeagueSummary id={league.id} key={league.id}>
                  {league.name}
                </LeagueSummary>
              ))}
            </ul>
            <Button variant="raised" color="primary" className="RefreshButton" onClick={this.update}>
              Refresh Leagues
            </Button>
          </div>
        </div>
      );
    }
  }
}

export default LeagueList;
