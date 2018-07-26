import React, { Component } from 'react';
import AutoForm from 'uniforms-material/AutoForm';
import SimpleSchema2 from 'simpl-schema';
import axios from 'axios';
import Header from '../other/Header';
import Error from '../other/Error';

export default class AddLeague extends Component {
  constructor() {
    super();
    this.state = { err: null };
    this.submit = this.submit.bind(this);
  }

  submit(caller) {
    axios.post('/api/league', { name: caller.leagueName })
      .then(() => {
        window.location.href = '/league';
      })
      .catch((err) => {
        if (err.response.status === 401) {
          window.location.href = '/login';
        } else {
          this.setState({ err });
        }
      });
  }

  render() {
    const { err } = this.state;
    if (err !== null) {
      return (
        <div>
          <Header title="Add a league!" back="Back to Leagues" backLocation="/league" />
          <Error error={err} />
        </div>
      );
    }
    return (
      <div>
        <Header title="Add a league!" back="Back to Leagues" backLocation="/league" />
        <AutoForm
          onSubmit={this.submit}
          schema={new SimpleSchema2({
            leagueName: {
              type: String,
            },
          })}
        />
      </div>
    );
  }
}
