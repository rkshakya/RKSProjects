<?php defined('_JEXEC') or die('Restricted access'); 
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left">
				<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    //dump($this->subscribers, "SUBSCRIBERS");
    foreach($this->subscribers as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name).'('.$value->sub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'sub_code', '', 'value', 'text', $this->lists['subcode']);
				?>
			</td>
			
			<td align="left">
				<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A PUBLICATION'));
    //dump($this->publications, "PUBLICATIONS");
    foreach($this->publications as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->pub_code, JText::_($value->pub_name).'('.$value->pub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'pub_code', '', 'value', 'text', $this->lists['pubcode']);
				?>
				</td>
				
				<td align="left">
				<?php 
				$options = array(); 
				$options[] = JHTML::_('select.option','DEFAULT',JText::_('PLS CHOOSE ISSUE'));   
    foreach($this->issues as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->delivery_issue, JText::_($value->delivery_issue) );
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'delivery_issue', '', 'value', 'text', $this->lists['deliveryissue']);
				?>
				</td>
				<td align="left">
				<?php 
				$optionsFreq = array(JHTML::_('select.option','',JText::_('PLS CHOOSE TIMEFRAME')),
				  JHTML::_('select.option', 7 , JText::_('past 1 week')),
				  JHTML::_('select.option', 14, JText::_('past 2 weeks')),
				  JHTML::_('select.option', 30, JText::_('past 1 month')),
				  JHTML::_('select.option', 90, JText::_('past 3 months')),
				  JHTML::_('select.option', 180, JText::_('past 6 months')),
				  JHTML::_('select.option', 365, JText::_('past 1 year'))		
				);
    echo JHTML::_('select.genericlist', $optionsFreq, 'time_period', '', 'value', 'text', $this->lists['timeperiod']);
				?>
			</td>
				
				<td align="left">
				<button onclick="this.form.submit();"><?php echo JText::_( 'Go' ); ?></button>
				<button onclick="document.getElementById('sub_code').value='';document.getElementById('pub_code').value='';document.getElementById('delivery_issue').value='DEFAULT';document.getElementById('time_period').value='';this.form.submit();"><?php echo JText::_( 'Reset' ); ?></button>
			</td>
	</tr>
</table>
<?php if(!empty($this->lists)) { ?>
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'Database ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
    <?php echo JHTML::_('grid.sort', JText::_('Delivery Date'),'delivery_date', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JHTML::_('grid.sort', JText::_('Order Code'),'order_code', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
			<?php echo JHTML::_('grid.sort', JText::_('Subscriber'),'sub_name', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
			<?php echo JHTML::_('grid.sort', JText::_('Publication'),'pub_name', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Title' ); ?>
			</th>
			<th>
	  <?php echo JHTML::_('grid.sort', JText::_('Issue'),'delivery_issue', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
	  <?php echo JHTML::_('grid.sort', JText::_('Issue Date'),'delivery_issuedt', $this->lists['order_Dir'], $this->lists['order'] ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Copies' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Delivered by' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Received by' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Delivered' ); ?>
			</th>					
			<th>
				<?php echo JText::_( 'Created/Modified Date' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->delivery_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=delivery&task=edit&cid[]='. $row->delivery_id );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->delivery_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->delivery_date; ?>
			</td>
			<td>
				<?php echo $row->order_code; ?>
			</td>
			<td>
				<?php echo $row->sub_name; ?>
			</td>	
			<td>
				<?php echo $row->pub_name; ?>
			</td>
			<td>
				<?php echo $row->delivery_title; ?>
			</td>
			<td>
				<?php echo $row->delivery_issue; ?>
			</td>
			<td>
				<?php echo $row->delivery_issuedt; ?>
			</td>
			<td>
				<?php echo $row->delivery_copies; ?>
			</td>
			<td>
				<?php echo $row->delivered_by; ?>
			</td>
			<td>
				<?php echo $row->received_by; ?>
			</td>		
			<td>
				<?php echo $row->delivery_done; ?>
			</td>								
			<td>
				<?php echo $row->cdate.'('.$row->mdate.')'; ?>
			</td>
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="16"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
	<?php } ?>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="delivery" />
<input type="hidden" name="filter_order" value="<?php echo $this->lists['order']; ?>" />
<input type="hidden" name="filter_order_Dir" value="<?php echo $this->lists['order_Dir']; ?>" />
</form>
