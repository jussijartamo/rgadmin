'use strict';

import './style.less';
import CodeArea from './codearea.jsx';
import NavBar from './navbar.jsx';
import ExceptionPopover from './exception.jsx';
import ConnectionsPopover from './connections.jsx';
import React from 'react';
import { render } from 'react-dom';
import { Button, Modal } from 'react-bootstrap';
import LocalStorageMixin from 'react-localstorage';
import mixin from 'es6-react-mixins';
import Binder from 'react-binding';

const Page = module.exports = React.createClass({
    displayName: 'Page',
    mixins: [LocalStorageMixin],
    getInitialState: function() {
        return {wait: false, loading: false, vars: [], connections: [
            {
                id: "test",
                host: "localhost",
                port: 5432,
                database: "test",
                username: "test",
                password: "test"
            }
        ]};
    },
    getDefaultProps: function() {
      return {
        stateFilterKeys: ['code', 'connections']
      };
    },
    onChange: function(value) {
        this.setState({code: value});
    },
    close: function() {
        this.setState({wait: false, showExceptionModal: false, showConnectionsModal: false});
    },
    run: function(event) {
        this.stopPropagation(event);
        const output = Babel.transform(this.state.code, { presets: ['es2015'] }).code;
        this.setState({wait: true, loading: true});
        const me = this;
        const formData = new FormData();
        formData.append('script', output);
        formData.append('connection', this.state.selectedConnectionModel ? JSON.stringify(this.state.selectedConnectionModel.sourceObject) : '');
        $.ajax({
                url: 'api/v1/execute',
                type: 'POST',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: (data) => {
                  me.setState({vars: data.vars});
                  me.setState({wait: false});
                }
        })
        /*$.post( , formData, (data) => {
            me.setState({vars: data.vars});
            me.setState({wait: false});
        })*/
        .fail(function(req) {
            const jsonResponse = JSON.parse(req.responseText);
            me.setState({
                showExceptionModal: true,
                message: jsonResponse.message,
                lines: jsonResponse.lines,
                lineNumber: jsonResponse.lineNumber
            });
        }).always(function() {
            me.setState({loading: false});
        });
    },
    componentDidMount: function() {
        $(document).keydown(this.handleKeyDown);
    },
    handleKeyDown: function(e) {
        if (e.keyCode==13 && e.ctrlKey) {
            this.run(e);
        }
    },
    showConnections: function(event) {
        this.stopPropagation(event);
        this.setState({wait: true, showConnectionsModal: true});
    },
    stopPropagation: function(event) {
        event.preventDefault(); // Let's stop this event.
        event.stopPropagation(); // Really this time.
    },
    handleSelectConnection: function(connectionModel, event) {
        this.stopPropagation(event);
        const neitherIsNull = (this.state.selectedConnectionModel && connectionModel);
        const selectedTwice = (neitherIsNull && this.state.selectedConnectionModel.sourceObject === connectionModel.sourceObject);
        this.setState({selectedConnectionModel: selectedTwice ? null : connectionModel});
    },
    render: function() {
        const selectedConnection = this.state.selectedConnectionModel ?
                        this.state.selectedConnectionModel.sourceObject : null;
        return <div>
                <NavBar run={this.run}
                        connections={this.showConnections}
                        selectedConnection={selectedConnection}
                        blur={this.state.wait}/>
                <div className="modal-container">
                    <ExceptionPopover   showModal={this.state.showExceptionModal}
                                        message={this.state.message}
                                        lines={this.state.lines}
                                        lineNumber={this.state.lineNumber}
                                        close={this.close}/>
                    <ConnectionsPopover showModal={this.state.showConnectionsModal}
                                        connections={Binder.bindArrayToState(this, "connections")}
                                        handleSelectConnection={this.handleSelectConnection}
                                        selectedConnectionModel={this.state.selectedConnectionModel}
                                        close={this.close}/>
                </div>
                { this.state.loading ? <div className="wait"><img src="gears.gif"/></div> : null }
                <CodeArea   blur={this.state.wait}
                            value={this.state.code ? this.state.code : ""}
                            readOnly={this.state.wait}
                            onChange={this.onChange}
                            results={this.state.vars}/>

               </div>;
    }
});

render(
  <Page/>,
  document.getElementById('app')
);