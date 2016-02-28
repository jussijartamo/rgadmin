'use strict';

import React from 'react';
import { render } from 'react-dom';
import { Button, Modal, Popover, Tooltip, OverlayTrigger  } from 'react-bootstrap';
import Binder from 'react-binding';

const ConnectionsPopover = module.exports = React.createClass({
  getInitialState: function() {
         return {};
  },
  handleAddConnection: function() {
    this.props.connections.add({id:"New connection2"});
  },
  handleRemoveConnection: function(event) {
    this.props.connections.remove(this.props.selectedConnectionModel.sourceObject);
    this.handleSelectConnection(null,event);
  },
  handleSelectConnection: function(connection,event) {
    this.props.handleSelectConnection(connection,event)
  },
  render: function() {
    const connections = this.props.connections;
    const connection = this.props.selectedConnectionModel;
    const handleSelectConnection = this.handleSelectConnection;
    return <Modal bsSize="lg" show={this.props.showModal} onHide={this.props.close}>
                <Modal.Header closeButton>
                  <Modal.Title>Connections</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <div className="row">
                    <div className="col-xs-4">
                      <ul className="nav nav-pills nav-stacked">
                      {connections.items.map(function(c, index) {
                        const icon = connection && connection.sourceObject === c.sourceObject ? <i className="fa fa-plug">&nbsp;</i> : null
                        return <li key={index}><a href="#" onClick={handleSelectConnection.bind(null, c)}>{icon}{c.sourceObject.id}</a></li>
                      })}
                      </ul>
                    </div>
                    <div className="col-xs-8">
                        {connection ? <div className="row"><form className="form-horizontal">
                            <div className="form-group">
                                <label htmlFor="connectionId" className="col-xs-3 control-label">Id</label>
                                <div className="col-xs-9">
                                    <input type="text" valueLink={Binder.bindTo(connection,"id")} className="form-control" id="connectionId" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="host" className="col-xs-3 control-label">Host</label>
                                <div className="col-xs-9">
                                    <input type="text" valueLink={Binder.bindTo(connection,"host")} className="form-control" id="host" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="port" className="col-xs-3 control-label">Port</label>
                                <div className="col-xs-9">
                                    <input type="text" valueLink={Binder.bindTo(connection,"port")} className="form-control" id="port" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="database" className="col-xs-3 control-label">Database</label>
                                <div className="col-xs-9">
                                    <input type="text" valueLink={Binder.bindTo(connection,"database")} className="form-control" id="database" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="username" className="col-xs-3 control-label">Username</label>
                                <div className="col-xs-9">
                                    <input type="text" valueLink={Binder.bindTo(connection,"username")} className="form-control" id="username" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="password" className="col-xs-3 control-label">Password</label>
                                <div className="col-xs-9">
                                    <input type="password" valueLink={Binder.bindTo(connection,"password")} className="form-control" id="password" placeholder=""/>
                                </div>
                            </div>
                            <div className="form-group">
                                <div className="col-xs-offset-3 col-xs-9">
                                  <button type="button" onClick={this.handleRemoveConnection} className="btn btn-default">Remove connection '{connection.sourceObject.id}'</button>
                                </div>
                            </div>

                        </form></div> : null}
                    </div>
                  </div>
                </Modal.Body>
                <Modal.Footer>
                  <Button className="pull-left" onClick={this.handleAddConnection}>Add new connection</Button>
                  <Button onClick={this.props.close}>Close</Button>
                </Modal.Footer>
              </Modal>;
  }
});