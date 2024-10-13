import { Card, CardBody, CardText } from 'reactstrap';
import React from 'react';

export const CommonNameCard = ({ key, id, profilePic, nickname, jobRole }) => {
  return (
    <>
      <Card
        style={{
          border: '2px solid navy',
          borderRadius: '10px',
          width: '300px',
          padding: '10px',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
        }}
      >
        {/* Display Picture Placeholder */}
        <div
          style={{
            width: '50px',
            height: '50px',
            borderRadius: '50%',
            backgroundColor: '#ccc',
            marginRight: '15px',
          }}
        ></div>
        {/* Nickname and Job Role */}
        <CardBody style={{ padding: '0' }}>
          <h5 style={{ margin: '0', color: 'navy' }}>{nickname}</h5>
          <CardText style={{ margin: '0', color: '#666' }}>{jobRole}</CardText>
        </CardBody>
      </Card>
    </>
  );
};

export default CommonNameCard;
