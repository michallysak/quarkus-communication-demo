import { ApplicationConfig, inject, InjectionToken } from '@angular/core';
import { ApolloClientOptions, ApolloClient, InMemoryCache } from '@apollo/client/core';
import { Apollo, APOLLO_OPTIONS } from 'apollo-angular';
import { HttpLink } from 'apollo-angular/http';
import { WebSocketLink } from '@apollo/client/link/ws';

export const GRAPHQL_SUBSCRIPTION_CLIENT = new InjectionToken<ApolloClient<any>>('SUBSCRIPTION_CLIENT');

export function apolloOptionsFactory(): ApolloClientOptions<any> {
  const httpLink = inject(HttpLink);
  return {
    link: httpLink.create({ uri: 'http://localhost:8080/graphql' }),
    cache: new InMemoryCache(),
    defaultOptions: {
      watchQuery: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'ignore',
      },
      query: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'all',
      },
      mutate: {
        errorPolicy: 'all',
      },
    },
  };
}

export function subscriptionClientFactory(): ApolloClient<any> {
  return new ApolloClient({
    ...apolloOptionsFactory(),
    link: new WebSocketLink({
      uri: 'ws://localhost:8080/graphql',
      options: {
        reconnect: true,
      },
    }),
  });
}

export const graphqlProvider: ApplicationConfig['providers'] = [
  Apollo,
  {
    provide: APOLLO_OPTIONS,
    useFactory: apolloOptionsFactory,
  },
  {
    provide: GRAPHQL_SUBSCRIPTION_CLIENT,
    useFactory: subscriptionClientFactory,
  },
];
