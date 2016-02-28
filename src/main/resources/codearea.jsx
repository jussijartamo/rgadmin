
import React from 'react';
import ReactDOM from 'react-dom';
import AceEditor from 'react-ace';
import brace from 'brace';
import Result from './result.jsx';

import 'brace/mode/javascript';
import 'brace/theme/xcode';

export default class CodeArea extends React.Component {
    componentDidMount() {
        const touchSplitter1 = $('.split-me').touchSplit({
            leftMin:100,
            rightMin:300,
            thickness: "12px",
            dock:"right"
        });
    }
    render() {
        return <div id="panels" className={ this.props.blur ? "blur" : null }>
                <div className="row">
                    <div className="col-md-12 split-me-container">
                        <div className="split-me">
                           <AceEditor  name="codearea"
                                       mode="javascript"
                                       theme="xcode"
                                       value={this.props.value}
                                       readOnly={this.props.readOnly}
                                       onChange={this.props.onChange}
                                       editorProps={{$blockScrolling: true}}/>
                           <Result vars={this.props.results}/>
                        </div>
                    </div>
                </div>
               </div>;
    }
};
