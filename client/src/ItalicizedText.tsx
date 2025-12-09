import React from 'react';

interface ItalicizedTextProps {
  text: string;
}

const ItalicizedText: React.FC<ItalicizedTextProps> = ({ text }) => {
  // Split the string by parts enclosed in underscores.
  // The regex will capture the delimiters (the parts with underscores).
  const parts = text.split(/(_.*?_)/g);

  return (
    <p>
      {parts.map((part, index) => {
        if (part.startsWith('_') && part.endsWith('_')) {
          // If the part is enclosed in underscores, render it as italic.
          return <i key={index}>{part.slice(1, -1)}</i>;
        }
        // Otherwise, render it as plain text.
        return part;
      })}
    </p>
  );
};

export default ItalicizedText;

