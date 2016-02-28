'use strict';

import React from 'react';
import { render } from 'react-dom';
import { Button, Modal, Popover, Tooltip, OverlayTrigger  } from 'react-bootstrap';

export default class ExceptionPopover extends React.Component {

  constructor(props) {
    super()
  }

  render() {
    return <Modal show={this.props.showModal} onHide={this.props.close}>
                <Modal.Header closeButton>
                  <Modal.Title>Script execution failed!</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <p>Execution failed with message: {this.props.message}</p>
                  {this.props.lines ?
                  <pre>{this.props.lines.map((some,index)=> {
                      return <span className={this.props.lineNumber == index + 1 ? "danger": null} key={index}>{some}</span>;
                  })}</pre>: null}
                </Modal.Body>
                <Modal.Footer>
                  <Button onClick={this.props.close}>Close</Button>
                </Modal.Footer>
              </Modal>;
  }
};