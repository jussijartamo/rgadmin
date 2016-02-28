
import React from 'react';
import ReactDOM from 'react-dom';

export default class Result extends React.Component {
  render() {
    /*<p className="no-results">Query results</p>*/
    return <div className="results">
                <p className="no-results">Query results</p>
                {this.props.vars.map(function(v, ind3){
                    if(v.items) {
                        return <table key={ind3} className="table">
                            <thead>
                                <tr><th>{v.name}</th></tr>
                            </thead>
                            <tbody>
                                {v.items.map((some,index)=> {
                                    return <tr key={index}><td>{some}</td></tr>;
                                })}
                            </tbody>
                        </table>;
                    } else if(v.rows) {
                        const span = v.rows.length > 0 ? v.rows[0].length : 1;
                        return <table key={ind3} className="table">
                            <thead>
                                <tr>
                                    <th className="name" colSpan={span}>{v.name}</th>
                                </tr>
                                {v.columns ? <tr>
                                    {v.columns.map((some)=> {
                                        return <th>{some}</th>;
                                    })}
                                </tr> : null }
                            </thead>
                            <tbody>
                                {v.rows.map((some,index)=> {
                                    return <tr key={index}>{some.map((cell, index2)=> {
                                          return <td key={index2}>{cell}</td>;
                                    })}</tr>;
                                })}
                            </tbody>
                        </table>;
                    } else {
                        return <div key={ind3}><var><strong>{v.name}</strong></var> = <var>{v.value}</var></div>;
                    }
                })}
           </div>;
  }
};
