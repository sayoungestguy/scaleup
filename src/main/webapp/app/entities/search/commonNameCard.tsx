import { Card, CardBody, CardText } from 'reactstrap';
import React from 'react';

export const CommonNameCard = ({ key, id, profilePic, nickname, jobRole }) => {
  return (
    <>
      <Card
        style={{
          border: '2px solid navy',
          borderRadius: '10px',
          width: '45%',
          padding: '10px',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          marginRight: '2rem',
        }}
      >
        {/* Display Picture Placeholder */}
        <img
          src={
            profilePic === 'Male Profile Picture'
              ? 'https://images2.imgbox.com/58/df/QqnDcnpM_o.png'
              : 'https://images2.imgbox.com/b3/e3/eB7ZJyVa_o.png'
          }
          alt="Profile Picture"
          style={{ width: '50px', height: '50px', borderRadius: '50%', marginRight: '15px' }}
        />
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
