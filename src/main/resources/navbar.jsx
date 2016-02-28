
import React from 'react';
import ReactDOM from 'react-dom';

export default class NavBar extends React.Component {
  render() {
    const navBarClasses = "navbar navbar-default"
    return <nav className={ this.props.blur ? "blur " + navBarClasses : navBarClasses }>
               <ul className="nav navbar-nav">
                 <li onClick={this.props.run}><a href="#"><i className="fa fa-play" />&nbsp;Run Ctrl+&#x23ce;</a></li>
                 <li onClick={this.props.connections}>
                    {this.props.selectedConnection ?
                        <a href="#"><i className="fa fa-plug"/>&nbsp;Using connection '{this.props.selectedConnection.id}'</a> :
                        <a href="#"><i className="fa fa-plug"/>&nbsp;Connect to database</a>
                    }
                 </li>
               </ul>
           </nav>;
  }
};
