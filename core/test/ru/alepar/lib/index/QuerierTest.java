package ru.alepar.lib.index;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;
import ru.alepar.lib.traum.ItemStorage;

@RunWith(JMock.class)
public class QuerierTest {

    private final Mockery mockery = new JUnit4Mockery();

    private final Index index = mockery.mock(Index.class);
    private final ItemStorage storage = mockery.mock(ItemStorage.class);
    private final Querier querier = new Querier(index, storage);

}
