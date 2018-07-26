import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import deepPurple from '@material-ui/core/colors/deepPurple';
import indigo from '@material-ui/core/colors/indigo';
// import grey from '@material-ui/core/colors/grey';
// import CssBaseline from '@material-ui/core/CssBaseline';
import LeagueList from './other/LeagueList';
import AddLeague from './add/AddLeague';
import '../resources/style/style.scss';

const theme = createMuiTheme({
  palette: {
    // type: 'dark',
    primary: { main: deepPurple['500'] },
    secondary: { main: indigo.A400 },
    // background: { default: grey['900'] },
  },
});

function App() {
  return (
    <MuiThemeProvider theme={theme}>
      {/* <CssBaseline /> */}
      <Router>
        <div id="app">
          <Route exact path="/" component={LeagueList} />
          <Route exact path="/league" component={LeagueList} />
          <Route exact path="/league/add" component={AddLeague} />
        </div>
      </Router>
    </MuiThemeProvider>
  );
}

ReactDOM.render(
  <App />,
  document.getElementById('react'),
);
