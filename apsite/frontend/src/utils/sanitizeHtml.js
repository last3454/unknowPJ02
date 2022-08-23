import sanitizeHtml from 'sanitize-html'
//sanitizeHtml.defaults.allowedAttributes['*'] = ['src:data', 'class', 'style']
const sanitizeOption = {
  allowedTags: sanitizeHtml.defaults.allowedTags.concat(['img', 'p']),
  allowedAttributes: {
     a: ['href', 'name', 'target'],
     img: ['src','width','height'],
     li: ['class'],
     ['*'] : ['src:data', 'class', 'style']
  },
  allowedSchemes: ['data', 'http'],
};

export default (content) => {
  return sanitizeHtml(content, sanitizeOption);
};